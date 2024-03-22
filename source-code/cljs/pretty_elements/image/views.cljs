
(ns pretty-elements.image.views
    (:require [dynamic-props.api                :as dynamic-props]
              [fruits.random.api                :as random]
              [lazy-loader.api                  :as lazy-loader]
              [pretty-accessories.badge.views   :as badge.views]
              [pretty-accessories.cover.views   :as cover.views]
              [pretty-accessories.icon.views    :as icon.views]
              [pretty-accessories.label.views   :as label.views]
              [pretty-accessories.marker.views  :as marker.views]
              [pretty-accessories.tooltip.views :as tooltip.views]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-elements.image.attributes :as image.attributes]
              [pretty-elements.image.prototypes :as image.prototypes]
              [pretty-elements.methods.api      :as pretty-elements.methods]
              [pretty-models.api                :as pretty-models]
              [pretty-subitems.api              :as pretty-subitems]
              [reagent.core                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:badge   badge.views/SHORTHAND-MAP
                    :cover   cover.views/SHORTHAND-MAP
                    :icon    icon.views/SHORTHAND-KEY
                    :label   label.views/SHORTHAND-KEY
                    :tooltip tooltip.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn broken-image
  ; @ignore
  []
  [:svg {:xmlns "http://www.w3.org/2000/svg" :height "24px" :width "24p0" :view-box "0 0 24 24" :fill "#000"}
        [:path {:fill "none" :d "M0 0h24v24H0z"}]
        [:path {:fill "none" :d "M0 0h24v24H0zm0 0h24v24H0zm21 19c0 1.1-.9 2-2 2H5c-1.1 0-2-.9-2-2V5c0-1.1.9-2 2-2h14c1.1 0 2 .9 2 2"}]
        [:path {             :d "M21 5v6.59l-3-3.01-4 4.01-4-4-4 4-3-3.01V5c0-1.1.9-2 2-2h14c1.1 0 2 .9 2 2zm-3 6.42l3 3.01V19c0 1.1-.9 2-2 2H5c-1.1 0-2-.9-2-2v-6.58l3 2.99 4-4 4 4 4-3.99z"}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :loaded? (boolean)(opt)
  ;  :marker (map)(opt)
  ;  :sensor (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [badge cover icon label loaded? marker sensor tooltip] :as props}]
  [:div (image.attributes/outer-attributes id props)
        [(pretty-models/click-control-auto-tag props) (image.attributes/inner-attributes id props)
         (when   loaded? [:div (image.attributes/canvas-attributes id props)])
         (if-not loaded? [lazy-loader/image-sensor (pretty-subitems/subitem-id id :sensor)  sensor])
         (if-not loaded? [icon.views/view          (pretty-subitems/subitem-id id :icon)    icon])
         (when   label   [label.views/view         (pretty-subitems/subitem-id id :label)   label])
         (when   badge   [badge.views/view         (pretty-subitems/subitem-id id :badge)   badge])
         (when   marker  [marker.views/view        (pretty-subitems/subitem-id id :marker)  marker])
         (when   cover   [cover.views/view         (pretty-subitems/subitem-id id :cover)   cover])
         (when   tooltip [tooltip.views/view       (pretty-subitems/subitem-id id :tooltip) tooltip])]])

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
                         :reagent-render         (fn [_ props] [image id props])}))

(defn view
  ; @description
  ; Optionally clickable image element with built-in lazy loader, animated loading icon, and optional keypress control.
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
  ; [Image canvas model](pretty-core/cljs/pretty-models/api.html#image-canvas-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/image.png)
  ; [image {:background-size :cover
  ;         :background-uri  "/my-image.png"
  ;         :badge           {:icon {:icon-name :fullscreen} :position :tr}
  ;         :border-radius   {:all :s}
  ;         :label           {:content "My image"}
  ;         :outer-height    :s
  ;         :outer-width     :l}]
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
             props (image.prototypes/props-prototype                       id props)
             props (dynamic-props/import-props                             id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
