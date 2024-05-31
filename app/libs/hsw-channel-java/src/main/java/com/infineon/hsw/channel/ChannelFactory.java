// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * Factory class to create communication channels.
 */
public final class ChannelFactory {
    private static final String EXCEPTION_MESSAGE_FACTORY_ALREADY_REGISTER =
            "Factory with this name already registered";

    /**
     * Private constructors for class.
     */
    private ChannelFactory() {
    }

    /**
     * * List containing channel factory registry.
     */
    private static final List<IChannelProvider> factoryRegistry = new ArrayList<>();

    /**
     * Map of channel names versus provider.
     */
    private static final HashMap<String, IChannelProvider> channels = new HashMap<>();

    /**
     * Get a channel of the requested type and name. If the channel requires
     * additional properties for
     * creation, the properties can be specified in an additional parameter.
     *
     * @param channelType       type which determines the factory used to
     *         available types can be determined by the (getChannelTypes)
     * method.
     * @param channelName       user friendly name for channel. The available
     *         names for a specific type can be obtained by getChannelNames().
     * @param channelProperties additional properties possibly required for a
     *         specific type of channel or null.
     * @return Communication channel of requested type and name or null if
     *         channel could not be created.
     * @throws ChannelException if factory cannot be found.
     */
    public static IChannel getChannel(final String channelType,
                                      final String channelName,
                                      final String channelProperties)
            throws ChannelException {
        IChannelProvider factory = lookupProvider(channelType);

        if (factory != null) {
            return factory.getChannel(channelName, channelProperties);
        }

        throw new ChannelException("Factory not found");
    }

    /**
     * Get a channel of the requested name. The registered factories are asked
     * to create the channel in the order of their registration. The returned
     * instance is the object created by the first factory providing the named
     * channel.
     *
     * @param channelName User friendly name for channel. The available names
     *         for a specific type can be obtained by getChannelNames().
     * @return Communication channel of name or null if channel could not be
     *         created.
     */
    public static IChannel getChannel(final String channelName) {
        IChannel channel = null;
        // force refresh of channel names
        getChannelNames();

        // get provider for this channel name and try to create channel
        IChannelProvider provider = channels.get(channelName);
        if (provider != null) {
            channel = provider.getChannel(channelName, null);
        }
        // return channel or null if it could not be created
        return channel;
    }

    /**
     * Return a list of channel types available in the factory.
     *
     * @return array of friendly channel types.
     */
    public static String[] getChannelTypes() {
        Enumeration<IChannelProvider> factory = Collections.enumeration(
                factoryRegistry);
        String[] channelTypes = new String[factoryRegistry.size()];

        for (int i = 0; i < channelTypes.length; i++) {
            channelTypes[i] = factory.nextElement().getProviderName();
        }

        return channelTypes;
    }

    /**
     * Return a list of channel names for the given type.
     *
     * @param channelType type of channel it also refer as name of channel
     *         factory.
     *
     * @return array of friendly channel names.
     */
    public static String[] getChannelNames(final String channelType) {
        IChannelProvider factory = lookupProvider(channelType);

        if (factory != null) {
            return factory.getChannelNames();
        }

        return new String[0];
    }

    /**
     * Return a list of channel names for all registered factories. Each name
     * only appears once even if more than one factory provides a channel with
     * the same name.
     *
     * @return array of friendly channel names.
     */
    public static String[] getChannelNames() {
        channels.clear();
        String[] names;
        Enumeration<IChannelProvider> factoryList = Collections.enumeration(
                factoryRegistry);
        while (factoryList.hasMoreElements()) {
            IChannelProvider factory = factoryList.nextElement();
            for (String strName : factory.getChannelNames()) {
                channels.put(strName, factory);
            }
        }

        names = channels.keySet().toArray(new String[0]);
        Arrays.sort(names);

        return names;
    }

    /**
     * Register a channel factory implementation at the global channel factory.
     *
     * @param factory channel factory to be added to the global channel factory.
     * @throws ChannelException if factory with same friendly name is already
     *         registered.
     */
    public static void registerProvider(final IChannelProvider factory)
            throws ChannelException {
        // Check for factory with this name already registered or not
        if (lookupProvider(factory.getProviderName()) == null) {
            factoryRegistry.add(factory);
        } else {
            throw new ChannelException(
                    EXCEPTION_MESSAGE_FACTORY_ALREADY_REGISTER);
        }
    }

    /**
     * Unregister a channel factory implementation at the global channel
     * factory.
     *
     * @param name Name of channel factory to be removed from global channel
     *         factory.
     * @return Reference of removed factory or null if no factory with the given
     *         name could be found.
     */
    public static IChannelProvider unregisterProvider(final String name) {
        IChannelProvider factory = lookupProvider(name);

        // look for factory
        if (factory != null) {
            factoryRegistry.remove(factory);
        }

        return factory;
    }

    /**
     * Method to find the channel factory by its name.
     *
     *
     *
     * @param channelType name of channel factory (channel type description)
     * @return channel factory object or null if not found.
     */
    public static IChannelProvider lookupProvider(final String channelType) {
        Enumeration<IChannelProvider> registry = Collections.enumeration(
                factoryRegistry);

        while (registry.hasMoreElements()) {
            IChannelProvider provider = registry.nextElement();

            if (provider.getProviderName().compareTo(channelType) == 0) {
                return provider;
            }
        }

        return null;
    }

    /**
     * Method to return the channel factory by its name.
     *
     * @param readerName name of the reader
     * @return the registered Channel provider with the given channel name
     */
    public static IChannelProvider getChannelProvider(final String readerName) {
        IChannelProvider channelProvider = null;
        for (IChannelProvider iChannelProvider : factoryRegistry) {
            channelProvider = iChannelProvider;
            if (Arrays.asList(channelProvider.getChannelNames())
                    .contains(readerName)) {
                return channelProvider;
            }
        }
        return channelProvider;
    }
}
