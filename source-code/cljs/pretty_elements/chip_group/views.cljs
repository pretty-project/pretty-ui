
(ns pretty-elements.chip-group.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [pretty-elements.chip-group.attributes :as chip-group.attributes]
              [pretty-elements.chip-group.prototypes :as chip-group.prototypes]
              [pretty-elements.chip.views            :as chip.views]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-elements.methods.api :as pretty-elements.methods]
              [reagent.core                               :as reagent]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:chips [chip.views/SHORTHAND-MAP]})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-group
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:chips (maps in vector)(opt)
  ;  ...}
  [id {:keys [chips] :as props}]
  [:div (chip-group.attributes/outer-attributes id props)
        [:div (chip-group.attributes/inner-attributes id props)
              (letfn [(f0 [dex chip] [chip.views/view (pretty-subitems/subitem-id id dex) chip])]
                     (hiccup/put-with-indexed [:<>] chips f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [chip-group id props])}))

(defn view
  ; @description
  ; Group of downsized button elements.
  ;
  ; @links Implemented elements
  ; [Chip](pretty-ui/cljs/pretty-elements/api.html#chip)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/chip-group.png)
  ; ...
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (chip-group.prototypes/props-prototype                id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
