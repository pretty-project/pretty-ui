
(ns pretty-engine.api
    (:require [pretty-engine.diagram.focus.env :as diagram.focus.env]
              [pretty-engine.diagram.focus.side-effects :as diagram.focus.side-effects]
              [pretty-engine.diagram.keypress.side-effects :as diagram.keypress.side-effects]
              [pretty-engine.diagram.lifecycles.side-effects :as diagram.lifecycles.side-effects]
              [pretty-engine.diagram.state.env :as diagram.state.env]
              [pretty-engine.diagram.state.side-effects :as diagram.state.side-effects]
              [pretty-engine.diagram.utils :as diagram.utils]
              [pretty-engine.element.focus.env :as element.focus.env]
              [pretty-engine.element.focus.side-effects :as element.focus.side-effects]
              [pretty-engine.element.keypress.side-effects :as element.keypress.side-effects]
              [pretty-engine.element.lifecycles.side-effects :as element.lifecycles.side-effects]
              [pretty-engine.element.state.env :as element.state.env]
              [pretty-engine.element.state.side-effects :as element.state.side-effects]
              [pretty-engine.element.surface.env :as element.surface.env]
              [pretty-engine.element.surface.side-effects :as element.surface.side-effects]
              [pretty-engine.element.utils :as element.utils]
              [pretty-engine.input.focus.env :as input.focus.env]
              [pretty-engine.input.focus.side-effects :as input.focus.side-effects]
              [pretty-engine.input.keypress.side-effects :as input.keypress.side-effects]
              [pretty-engine.input.lifecycles.side-effects :as input.lifecycles.side-effects]
              [pretty-engine.input.options.env :as input.options.env]
              [pretty-engine.input.options.side-effects :as input.options.side-effects]
              [pretty-engine.input.popup.config :as input.popup.config]
              [pretty-engine.input.popup.env :as input.popup.env]
              [pretty-engine.input.popup.side-effects :as input.popup.side-effects]
              [pretty-engine.input.state.env :as input.state.env]
              [pretty-engine.input.state.side-effects :as input.state.side-effects]
              [pretty-engine.input.surface.env :as input.surface.env]
              [pretty-engine.input.surface.side-effects :as input.surface.side-effects]
              [pretty-engine.input.value.env :as input.value.env]
              [pretty-engine.input.value.side-effects :as input.value.side-effects]
              [pretty-engine.input.value.views :as input.value.views]
              [pretty-engine.input.props :as input.props]
              [pretty-engine.input.utils :as input.utils]
              [pretty-engine.layout.focus.env :as layout.focus.env]
              [pretty-engine.layout.focus.side-effects :as layout.focus.side-effects]
              [pretty-engine.layout.keypress.side-effects :as layout.keypress.side-effects]
              [pretty-engine.layout.lifecycles.side-effects :as layout.lifecycles.side-effects]
              [pretty-engine.layout.state.env :as layout.state.env]
              [pretty-engine.layout.state.side-effects :as layout.state.side-effects]
              [pretty-engine.layout.utils :as layout.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.diagram.focus.env/*)
(def diagram-focused? diagram.focus.env/diagram-focused?)

; @redirect (pretty-engine.diagram.focus.side-effects/*)
(def focus-diagram!             diagram.focus.side-effects/focus-diagram!)
(def blur-diagram!              diagram.focus.side-effects/blur-diagram!)
(def mark-diagram-as-focused!   diagram.focus.side-effects/mark-diagram-as-focused!)
(def unmark-diagram-as-focused! diagram.focus.side-effects/unmark-diagram-as-focused!)
(def diagram-focused            diagram.focus.side-effects/diagram-focused)
(def diagram-left               diagram.focus.side-effects/diagram-left)

; @redirect (pretty-engine.diagram.keypress.side-effects/*)
(def diagram-ENTER-pressed diagram.keypress.side-effects/diagram-ENTER-pressed)
(def diagram-ESC-pressed   diagram.keypress.side-effects/diagram-ESC-pressed)

; @redirect (pretty-engine.diagram.lifecycles.side-effects/*)
(def diagram-did-mount    diagram.lifecycles.side-effects/diagram-did-mount)
(def diagram-will-unmount diagram.lifecycles.side-effects/diagram-will-unmount)

; @redirect (pretty-engine.diagram.state.env/*)
(def get-diagram-state diagram.state.env/get-diagram-state)

; @redirect (pretty-engine.diagram.state.side-effects/*)
(def update-all-diagram-state! diagram.state.side-effects/update-all-diagram-state!)
(def update-diagram-state!     diagram.state.side-effects/update-diagram-state!)
(def clear-diagram-state!      diagram.state.side-effects/clear-diagram-state!)

; @redirect (pretty-engine.diagram.utils/*)
(def diagram-id->subitem-id diagram.utils/diagram-id->subitem-id)

; @redirect (pretty-engine.element.focus.env/*)
(def element-focused? element.focus.env/element-focused?)

; @redirect (pretty-engine.element.focus.side-effects/*)
(def focus-element!             element.focus.side-effects/focus-element!)
(def blur-element!              element.focus.side-effects/blur-element!)
(def mark-element-as-focused!   element.focus.side-effects/mark-element-as-focused!)
(def unmark-element-as-focused! element.focus.side-effects/unmark-element-as-focused!)
(def element-focused            element.focus.side-effects/element-focused)
(def element-left               element.focus.side-effects/element-left)

; @redirect (pretty-engine.element.keypress.side-effects/*)
(def element-ENTER-pressed element.keypress.side-effects/element-ENTER-pressed)
(def element-ESC-pressed   element.keypress.side-effects/element-ESC-pressed)

; @redirect (pretty-engine.element.lifecycles.side-effects/*)
(def element-did-mount    element.lifecycles.side-effects/element-did-mount)
(def element-will-unmount element.lifecycles.side-effects/element-will-unmount)

; @redirect (pretty-engine.element.state.env/*)
(def get-element-state element.state.env/get-element-state)

; @redirect (pretty-engine.element.state.side-effects/*)
(def update-all-element-state! element.state.side-effects/update-all-element-state!)
(def update-element-state!     element.state.side-effects/update-element-state!)
(def clear-element-state!      element.state.side-effects/clear-element-state!)

; @redirect (pretty-engine.element.surface.env/*)
(def element-surface-rendered? element.surface.env/element-surface-rendered?)

; @redirect (pretty-engine.element.surface.side-effects/*)
(def hide-element-surface! element.surface.side-effects/hide-element-surface!)
(def show-element-surface! element.surface.side-effects/show-element-surface!)

; @redirect (pretty-engine.element.utils/*)
(def element-id->subitem-id element.utils/element-id->subitem-id)

; @redirect (pretty-engine.input.focus.env/*)
(def input-focused? input.focus.env/input-focused?)

; @redirect (pretty-engine.input.focus.side-effects/*)
(def focus-input!             input.focus.side-effects/focus-input!)
(def blur-input!              input.focus.side-effects/blur-input!)
(def mark-input-as-focused!   input.focus.side-effects/mark-input-as-focused!)
(def unmark-input-as-focused! input.focus.side-effects/unmark-input-as-focused!)
(def input-focused            input.focus.side-effects/input-focused)
(def input-left               input.focus.side-effects/input-left)

; @redirect (pretty-engine.input.keypress.side-effects/*)
(def input-ENTER-pressed input.keypress.side-effects/input-ENTER-pressed)
(def input-ESC-pressed   input.keypress.side-effects/input-ESC-pressed)

; @redirect (pretty-engine.input.lifecycles.side-effects/*)
(def input-did-mount    input.lifecycles.side-effects/input-did-mount)
(def input-will-unmount input.lifecycles.side-effects/input-will-unmount)

; @redirect (pretty-engine.input.options.env/*)
(def get-input-options                 input.options.env/get-input-options)
(def render-input-option?              input.options.env/render-input-option?)
(def multiple-input-option-selectable? input.options.env/multiple-input-option-selectable?)
(def max-input-selection-not-reached?  input.options.env/max-input-selection-not-reached?)
(def get-picked-input-option           input.options.env/get-picked-input-option)
(def input-option-picked?              input.options.env/input-option-picked?)
(def input-option-toggled?             input.options.env/input-option-toggled?)
(def input-option-selected?            input.options.env/input-option-selected?)
(def get-input-option-color            input.options.env/get-input-option-color)

; @redirect (pretty-engine.input.options.side-effects/*)
(def pick-input-option!   input.options.side-effects/pick-input-option!)
(def toggle-input-option! input.options.side-effects/toggle-input-option!)
(def select-input-option! input.options.side-effects/select-input-option!)

; @redirect (pretty-engine.input.popup.config/*)
(def CLOSE-INPUT-POPUP-AFTER input.popup.config/CLOSE-INPUT-POPUP-AFTER)

; @redirect (pretty-engine.input.popup.env/*)
(def input-popup-rendered? input.popup.env/input-popup-rendered?)

; @redirect (pretty-engine.input.popup.side-effects/*)
(def close-input-popup!  input.popup.side-effects/close-input-popup!)
(def render-input-popup! input.popup.side-effects/render-input-popup!)
(def update-input-popup! input.popup.side-effects/update-input-popup!)

; @redirect (pretty-engine.input.state.env/*)
(def get-input-state input.state.env/get-input-state)

; @redirect (pretty-engine.input.state.side-effects/*)
(def update-all-input-state! input.state.side-effects/update-all-input-state!)
(def update-input-state!     input.state.side-effects/update-input-state!)
(def clear-input-state!      input.state.side-effects/clear-input-state!)

; @redirect (pretty-engine.input.surface.env/*)
(def input-surface-rendered? input.surface.env/input-surface-rendered?)

; @redirect (pretty-engine.input.surface.side-effects/*)
(def hide-input-surface! input.surface.side-effects/hide-input-surface!)
(def show-input-surface! input.surface.side-effects/show-input-surface!)

; @redirect (pretty-engine.input.value.env/*)
(def input-changed?            input.value.env/input-changed?)
(def input-empty?              input.value.env/input-empty?)
(def input-not-empty?          input.value.env/input-not-empty?)
(def get-input-internal-value  input.value.env/get-input-internal-value)
(def get-input-external-value  input.value.env/get-input-external-value)
(def get-input-displayed-value input.value.env/get-input-displayed-value)

; @redirect (pretty-engine.input.value.side-effects/*)
(def mark-input-as-changed!     input.value.side-effects/mark-input-as-changed!)
(def set-input-internal-value!  input.value.side-effects/set-input-internal-value!)
(def set-input-external-value!  input.value.side-effects/set-input-external-value!)
(def init-input-internal-value! input.value.side-effects/init-input-internal-value!)
(def use-input-initial-value!   input.value.side-effects/use-input-initial-value!)
(def input-value-changed        input.value.side-effects/input-value-changed)
(def empty-input!               input.value.side-effects/empty-input!)

; @redirect (pretty-engine.input.value.views/*)
(def input-synchronizer input.value.views/input-synchronizer)

; @redirect (pretty-engine.input.props/*)
(def input-label-props input.props/input-label-props)

; @redirect (pretty-engine.input.utils/*)
(def input-id->subitem-id input.utils/input-id->subitem-id)

; @redirect (pretty-engine.layout.focus.env/*)
(def layout-focused? layout.focus.env/layout-focused?)

; @redirect (pretty-engine.layout.focus.side-effects/*)
(def focus-layout!             layout.focus.side-effects/focus-layout!)
(def blur-layout!              layout.focus.side-effects/blur-layout!)
(def mark-layout-as-focused!   layout.focus.side-effects/mark-layout-as-focused!)
(def unmark-layout-as-focused! layout.focus.side-effects/unmark-layout-as-focused!)
(def layout-focused            layout.focus.side-effects/layout-focused)
(def layout-left               layout.focus.side-effects/layout-left)

; @redirect (pretty-engine.layout.keypress.side-effects/*)
(def layout-ENTER-pressed layout.keypress.side-effects/layout-ENTER-pressed)
(def layout-ESC-pressed   layout.keypress.side-effects/layout-ESC-pressed)

; @redirect (pretty-engine.layout.lifecycles.side-effects/*)
(def layout-did-mount    layout.lifecycles.side-effects/layout-did-mount)
(def layout-will-unmount layout.lifecycles.side-effects/layout-will-unmount)

; @redirect (pretty-engine.layout.state.env/*)
(def get-layout-state layout.state.env/get-layout-state)

; @redirect (pretty-engine.layout.state.side-effects/*)
(def update-all-layout-state! layout.state.side-effects/update-all-layout-state!)
(def update-layout-state!     layout.state.side-effects/update-layout-state!)
(def clear-layout-state!      layout.state.side-effects/clear-layout-state!)

; @redirect (pretty-engine.layout.utils/*)
(def layout-id->subitem-id layout.utils/layout-id->subitem-id)
