
# pretty-ui

### Overview

The <strong>pretty-ui</strong> is a ClojureScript/Reagent web application UI
component set made for websites and web applications using Re-Frame as state handler.

### Documentation

The <strong>pretty-ui</strong> functional documentation is [available here](documentation/COVER.md).

### deps.edn

```
{:deps {bithandshake/pretty-ui {:git/url "https://github.com/bithandshake/pretty-ui"
                                :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/pretty-ui/tree/release).

### Documentation

The <strong>pretty-ui</strong> functional documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>pretty-ui</strong> library [here](CHANGES.md).

# Usage

### Index

- [Event handlers](#event-handlers)

- [Content types](#content-types)

- [Value paths of inputs](#value-paths-of-inputs)

- [Options paths of optionable inputs](#options-paths-of-optionable-inputs)

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

### Value paths of inputs

Input elements and components can take `:value-path` property which points its
value stored in the Re-Frame state.

```
(ns my-namespace
    (:require [elements.api :as elements]))

[elements/text-field {:value-path [:my-value]}]
```

Every element or component that takes `:value-path` property can generates a path
in case of you don't want to specify it.

In the following example the text-field element uses its default value path:
`[:elements :element-handler/input-values *element-id*]`

```
(ns my-namespace
    (:require [elements.api :as elements]))

[elements/text-field {}]
```

### Options paths of optionable inputs
