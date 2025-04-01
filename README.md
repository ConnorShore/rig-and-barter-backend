<div align="center">
    <img src="https://objects.pc-rig-and-barter.com/rb-images-public/logo_transparent.png"  
        width="60%" alt="PC Rig and Barter Logo"/>
</div>
<br>

> [!NOTE]
> Link to demo site: [PC Rig and Barter Demo](https://demo.pc-rig-and-barter.com)

> [!NOTE]
> If you are interested in the architecture of the app, jump to the [Architecture Section](#architecture).

# Overview
PC Rig and Barter is a tool to help you design PC builds and acquire the different components you need to create your perfect build.

> [!NOTE]
> This project is still under development. New features and enhancements will be continually added and improved upon over time.

# Capabilities
PC Rig and Barter contains a variety of features to help you design PC builds as well as fostering a marketplace to buy and sell PC components and even manage the payment of the buying and selling process.

## Features
There are a variety of features PC Rig and Barter offer including

### Create and View Component Listings
This is the *Barter* portion of PC Rig and Barter.  Users are able to create listings for the PC components they would like to sell.  These listings are available for everyone to see, however you will need an account to *Request to Purchase* a listing.

<img src="https://pc-rb-public-assets.s3.us-west-1.amazonaws.com/ListingsPage.png" alt="Listing Gallery"/>

<img src="https://pc-rb-public-assets.s3.us-west-1.amazonaws.com/Listing.png" alt="Listing Details Page"/>

### Streamline flow to Purchase Listings
While other marketplaces require users to meet and pay in person for listed items, while that is still an option with PC Rig and Barter, this app also allows the purchasing of listed components to be facilitated directly through PC Rig and Barter.

When a buyer *Requests a Purchase* of a listing, a new *Transaction* is created between the buyer and seller to facilitate the purchase process. This creates a simple flow from purchase request to completion of the purchase.

<img src="https://pc-rb-public-assets.s3.us-west-1.amazonaws.com/Transactions.png" alt="Transaction Page"/>

To enable purchasing through the app directly, the user needs to connect a bank account through the app as well as set up a payment method (i.e. a credit card).  This make the user a ***Verified*** user and allows the *Transaction* to be handled through the app instead of in person.

<img src="https://pc-rb-public-assets.s3.us-west-1.amazonaws.com/PaymentInfo.png" alt="User Payment Info Page"/>

### User Messaging
When a *Transaction* is created, a new message group is created between the buyer and seller as well so they can communicate to answer any questions, negotiate on price, or to discuss a meetup location (if they aren't verified buyers/sellers).

<img src="https://pc-rb-public-assets.s3.us-west-1.amazonaws.com/Messages.png" alt="User Payment Info Page"/>

### PC Builder
This is the *Rig* portion of PC Rig and Barter.  Any user who has created an account has the ability to create their own PC builds.  This allows user to select components and save their configurations so they can work toward building their dream PC or just experiement with different builds.

<img src="https://pc-rb-public-assets.s3.us-west-1.amazonaws.com/PCBuilder.png" alt="PC Builder Page"/>

> [!NOTE]
> This feature is still being developed and will include some cool and necessary features such as compatability checking between components, AI component suggestions and more!

# Architecture

## Backend
The backend is a collection of microservices written in Java using Spring Boot 3 and deployed using Kubernetes.  Some additional libraries and technologies used include:
* **Kafka**: Used for events and messaging
* **Keycloak**: Used for authentication
* **Stripe**: Used for managing and controlling purchases and payments
* **

### Databases
Currently the databases being utilized are:
* **MySQL**
* **MongoDB**
* **MinIO (For Object Storage)**

The architecture of the services allow me to very easily swap to different database providers or types depending on my needs going forward, especially related to costs.

> [!NOTE]
> All the databases and services are self-hosted in my Homelab I created.  My Kubernetes Cluster is a multi-node cluster built with Raspberry PIs. My storage is split between a NAS where MongoDB and MySQL volumes are mounted.  MinIO is running in a Proxmox cluster.  My deployment pipeline agents are self-hosted as well.

## Frontend
The frontend is an Angular webapp written in typescript.  I utilized a frontend template as my frontend skills are not nearly as developed as my backend skills.  There aren't any major additional technologies used in the frontend, the only other technology being used is **SockJS** for websocket connections used for messages and notifications.