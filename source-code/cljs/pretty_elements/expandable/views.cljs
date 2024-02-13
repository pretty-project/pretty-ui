
(ns pretty-elements.expandable.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.button.views          :as button.views]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-elements.expandable.attributes :as expandable.attributes]
              [pretty-elements.expandable.prototypes :as expandable.prototypes]
              [pretty-elements.surface.views         :as surface.views]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [reagent.api                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  [:div (expandable.attributes/expandable-attributes expandable-id expandable-props)
        [:div (expandable.attributes/expandable-body-attributes expandable-id expandable-props)
              (let [button-id    (pretty-elements.engine/element-id->subitem-id expandable-id :button)
                    button-props (expandable.prototypes/button-props-prototype  expandable-id expandable-props)]
                   [button.views/view button-id button-props])
              (let [surface-id    (pretty-elements.engine/element-id->subitem-id expandable-id :surface)
                    surface-props (expandable.prototypes/surface-props-prototype expandable-id expandable-props)]
                   [surface.views/view surface-id surface-props])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    expandable-id expandable-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount expandable-id expandable-props))
                       :reagent-render         (fn [_ expandable-props] [expandable expandable-id expandable-props])}))

(defn view
  ; @description
  ; Expandable element for displaying content.
  ;
  ; @links Implemented elements
  ; [Button](pretty-ui/cljs/pretty-elements/api.html#button)
  ; [Surface](pretty-ui/cljs/pretty-elements/api.html#surface)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ; Check out the implemented elements below.
  ; Check out the implemented properties below.
  ;
  ; @usage (expandable.png)
  ; ...
  ([expandable-props]
   [view (random/generate-keyword) expandable-props])

  ([expandable-id expandable-props]
   ; @note (tutorials#parameterizing)
   (fn [_ expandable-props]
       (let [expandable-props (pretty-presets.engine/apply-preset                     expandable-id expandable-props)
             expandable-props (expandable.prototypes/expandable-props-prototype       expandable-id expandable-props)
             expandable-props (pretty-elements.engine/element-subitem<-disabled-state expandable-id expandable-props :button)
             expandable-props (pretty-elements.engine/element-subitem<-disabled-state expandable-id expandable-props :surface)
             expandable-props (pretty-elements.engine/leave-element-disabled-state    expandable-id expandable-props :button)
             expandable-props (pretty-elements.engine/leave-element-disabled-state    expandable-id expandable-props :surface)]
            [view-lifecycles expandable-id expandable-props]))))
