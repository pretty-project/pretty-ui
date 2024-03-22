
(ns pretty-elements.icon-button.views
    (:require [fruits.random.api                      :as random]
              [pretty-accessories.badge.views         :as badge.views]
              [pretty-accessories.cover.views         :as cover.views]
              [pretty-accessories.icon.views          :as icon.views]
              [pretty-accessories.label.views         :as label.views]
              [pretty-accessories.marker.views        :as marker.views]
              [pretty-accessories.tooltip.views       :as tooltip.views]
              [pretty-elements.engine.api             :as pretty-elements.engine]
              [pretty-elements.icon-button.attributes :as icon-button.attributes]
              [pretty-elements.icon-button.prototypes :as icon-button.prototypes]
              [pretty-elements.methods.api            :as pretty-elements.methods]
              [pretty-models.api                      :as pretty-models]
              [pretty-subitems.api                    :as pretty-subitems]
              [reagent.core                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:badge   badge.views/SHORTHAND-MAP
                    :cover   cover.views/SHORTHAND-MAP
                    :icon    icon.views/SHORTHAND-KEY
                    :label   label.views/SHORTHAND-KEY
                    :tooltip tooltip.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :marker (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [badge cover icon label marker tooltip] :as props}]
  [:div (icon-button.attributes/outer-attributes id props)
        [(pretty-models/click-control-auto-tag props) (icon-button.attributes/inner-attributes id props)
         (if icon    [icon.views/view    (pretty-subitems/subitem-id id :icon)   icon])
         (if label   [label.views/view   (pretty-subitems/subitem-id id :label)  label])
         (if badge   [badge.views/view   (pretty-subitems/subitem-id id :badge)  badge])
         (if marker  [marker.views/view  (pretty-subitems/subitem-id id :marker) marker])
         (if cover   [cover.views/view   (pretty-subitems/subitem-id id :cover)  cover])
         (if tooltip [tooltip.views/view (pretty-subitems/subitem-id id :cover)  cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   id props %))
                         :reagent-render         (fn [_ props] [icon-button id props])}))

(defn view
  ; @description
  ; Icon button element with optional keypress control, timeout lock, and progress display.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Icon](pretty-ui/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented models
  ; [Click control model](pretty-core/cljs/pretty-models/api.html#click-control-model)
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/icon-button.png)
  ; [icon-button {:border-radius {:all :s}
  ;               :fill-color    :highlight
  ;               :icon          {:icon-name :settings}
  ;               :gap           :xxs
  ;               :indent        {:all :xxs}
  ;               :label         {:content "My icon button"}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map    id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-preset           id props)
             props (pretty-elements.methods/import-element-dynamic-props   id props)
             props (pretty-elements.methods/import-element-focus-reference id props)
             props (pretty-elements.methods/import-element-state-events    id props)
             props (pretty-elements.methods/import-element-state           id props)
             props (pretty-elements.methods/import-element-timeout-events  id props)
             props (pretty-elements.methods/import-element-timeout         id props)
             props (icon-button.prototypes/props-prototype                 id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
