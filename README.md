
# pretty-ui

### Overview

The <strong>pretty-ui</strong> is a ClojureScript web application UI component set
made for web applications.

### Documentation

The <strong>pretty-ui</strong> functional documentation is [available here](documentation/COVER.md).

#### What's in this kit?

- 50+ UI elements

- 50+ UI components

- UI layout templates such as surfaces, popups, sidebars and menus

### Index

- [Event handlers](#event-handlers)

- [Content types](#content-types)

# Usage

### Event handlers

The components, elements and layouts take Re-Frame events as event handlers.

```
(ns my-namespace
    (:require [elements.api  :as elements]
              [re-frame.core :as r]))

(r/reg-event-fx :my-event (fn [_ _] {:dispatch [:do-something!]}))

[elements/button {:on-click [:my-event]}]
```

You could use metamorphic-events as well. You can find more information about them at:
[github.com/bithandshake/re-frame-api](github.com/bithandshake/re-frame-api)

```
(ns my-namespace
    (:require [elements.api :as elements]
              [re-frame.api :as r]))

(r/reg-event-fx :my-event (fn [_ _] [:do-something!]))

[elements/button {:on-click {:dispatch-n [[:my-event]]}}]
```

You can pass functions as handlers as well but the passed function will be evaluated
and its return value will be dispatched as a metamorphic-event, so make sure your
function returns a valid Re-Frame metamorphic-event or a `NIL` value.

```
(ns my-namespace
    (:require [elements.api :as elements]))

(defn my-function [] (do-something! ...))

[elements/button {:on-click my-function}]
```

### Content types
