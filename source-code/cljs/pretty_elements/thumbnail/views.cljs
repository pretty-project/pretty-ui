
(ns pretty-elements.thumbnail.views
    (:require [dynamic-props.api                    :as dynamic-props]
              [fruits.random.api                    :as random]
              [lazy-loader.api                      :as lazy-loader]
              [pretty-accessories.badge.views       :as badge.views]
              [pretty-accessories.cover.views       :as cover.views]
              [pretty-accessories.icon.views        :as icon.views]
              [pretty-accessories.label.views       :as label.views]
              [pretty-accessories.marker.views      :as marker.views]
              [pretty-accessories.tooltip.views     :as tooltip.views]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.methods.api          :as pretty-elements.methods]
              [pretty-elements.thumbnail.attributes :as thumbnail.attributes]
              [pretty-elements.thumbnail.prototypes :as thumbnail.prototypes]
              [pretty-models.api                    :as pretty-models]
              [pretty-subitems.api                  :as pretty-subitems]
              [reagent.core                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:badge   badge.views/SHORTHAND-MAP
                    :cover   cover.views/SHORTHAND-MAP
                    :icon    icon.views/SHORTHAND-KEY
                    :label   label.views/SHORTHAND-KEY
                    :tooltip tooltip.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- thumbnail
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
  [:div (thumbnail.attributes/outer-attributes id props)
        [(pretty-models/click-control-auto-tag props) (thumbnail.attributes/inner-attributes id props)
         (when   loaded? [:div (thumbnail.attributes/canvas-attributes id props)])
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
                         :reagent-render         (fn [_ props] [thumbnail id props])}))

(defn view
  ; @description
  ; Optionally clickable thumbnail element with built-in lazy loader, animated loading icon, and optional keypress control.
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
  ; @usage (pretty-elements/thumbnail.png)
  ; [thumbnail {:background-uri  "/my-thumbnail.png"
  ;             :badge           {:icon {:icon-name :fullscreen} :position :tr}
  ;             :border-radius   {:all :s}
  ;             :background-size :cover
  ;             :label           {:content "My thumbnail"}
  ;             :outer-height    :l
  ;             :outer-width     :l}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map    id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-presets          id props)
             props (pretty-elements.methods/import-element-dynamic-props   id props)
             props (pretty-elements.methods/import-element-focus-reference id props)
             props (pretty-elements.methods/import-element-state-events    id props)
             props (pretty-elements.methods/import-element-state           id props)
             props (pretty-elements.methods/import-element-timeout-events  id props)
             props (pretty-elements.methods/import-element-timeout         id props)
             props (thumbnail.prototypes/props-prototype                   id props)
             props (dynamic-props/import-props                             id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
