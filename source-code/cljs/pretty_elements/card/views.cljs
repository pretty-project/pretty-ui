
(ns pretty-elements.card.views
    (:require [fruits.random.api                :as random]
              [pretty-accessories.badge.views   :as badge.views]
              [pretty-accessories.cover.views   :as cover.views]
              [pretty-accessories.marker.views  :as marker.views]
              [pretty-accessories.tooltip.views :as tooltip.views]
              [pretty-elements.card.attributes  :as card.attributes]
              [pretty-elements.card.prototypes  :as card.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-elements.methods.api      :as pretty-elements.methods]
              [pretty-models.api                :as pretty-models]
              [pretty-subitems.api              :as pretty-subitems]
              [reagent.core                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)
(def SHORTHAND-MAP {:badge   badge.views/SHORTHAND-MAP
                    :cover   cover.views/SHORTHAND-MAP
                    :tooltip tooltip.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:badge (map)(opt)
  ;  :content (multitype-content)(opt)
  ;  :cover (map)(opt)
  ;  :marker (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [badge content cover marker tooltip] :as props}]
  [:div (card.attributes/outer-attributes id props)
        [(pretty-models/click-control-auto-tag props) (card.attributes/inner-attributes id props)
         (if content [:div (card.attributes/body-attributes id props) content])
         (if badge   [badge.views/view   (pretty-subitems/subitem-id id :badge)   badge])
         (if marker  [marker.views/view  (pretty-subitems/subitem-id id :marker)  marker])
         (if cover   [cover.views/view   (pretty-subitems/subitem-id id :cover)   cover])
         (if tooltip [tooltip.views/view (pretty-subitems/subitem-id id :tooltip) tooltip])]])

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
                         :reagent-render         (fn [_ props] [card id props])}))

(defn view
  ; @description
  ; Optionally clickable card style element.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented models
  ; [Click control model](pretty-core/cljs/pretty-models/api.html#click-control-model)
  ; [Flex content model](pretty-core/cljs/pretty-models/api.html#flex-content-model)
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/card.png)
  ; [card {:border-color     :muted
  ;        :border-radius    {:all :m}
  ;        :content          "My card"
  ;        :fill-color       :highlight
  ;        :horizontal-align :center
  ;        :vertical-align   :center
  ;        :outer-height     :l
  ;        :outer-width      :l}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [card "My card"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-key    id props SHORTHAND-KEY)
             props (pretty-elements.methods/apply-element-shorthand-map    id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-presets          id props)
             props (pretty-elements.methods/import-element-dynamic-props   id props)
             props (pretty-elements.methods/import-element-focus-reference id props)
             props (pretty-elements.methods/import-element-state-events    id props)
             props (pretty-elements.methods/import-element-state           id props)
             props (pretty-elements.methods/import-element-timeout-events  id props)
             props (pretty-elements.methods/import-element-timeout         id props)
             props (card.prototypes/props-prototype                        id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
