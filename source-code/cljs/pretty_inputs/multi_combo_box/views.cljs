
(ns pretty-inputs.multi-combo-box.views
    (:require [fruits.random.api                        :as random]
              [pretty-inputs.engine.api                 :as pretty-inputs.engine]
              [pretty-inputs.methods.api                :as pretty-inputs.methods]
              [pretty-inputs.multi-combo-box.attributes :as multi-combo-box.attributes]
              [pretty-inputs.multi-combo-box.prototypes :as multi-combo-box.prototypes]
              [pretty-subitems.api                      :as pretty-subitems]
              [reagent.core                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  [:div (multi-combo-box.attributes/outer-attributes id props)
        [:div (multi-combo-box.attributes/inner-attributes id props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/pseudo-input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/pseudo-input-will-unmount id props))
                         :reagent-render         (fn [_ props] [multi-combo-box id props])}))

(defn view
  ; @description
  ; ...
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ;
  ; @usage (pretty-inputs/multi-combo-box.png)
  ; ...
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props])))
