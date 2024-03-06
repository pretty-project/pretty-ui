
(ns components.item-list-row.views
    (:require [components.item-list-row.helpers    :as item-list-row.helpers]
              [components.item-list-row.prototypes :as item-list-row.prototypes]
              [fruits.css.api                      :as css]
              [fruits.random.api                   :as random]
              [multitype-content.api               :as multitype-content]
              [pretty-elements.api                 :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-cells
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:cells (vector)}
  [row-id {:keys [cells]}]
  (letfn [(f0 [cells cell-props]
              (conj cells [multitype-content/compose cell-props]))]
         (reduce f0 [:<>] cells)))

(defn- item-list-row
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:border (keyword)(opt)
  ;  :cells (Reagent components and/or Reagent component symbols in vector)}
  [row-id {:keys [border cells] :as row-props}]
  [:div.c-item-list-row (item-list-row.helpers/row-attributes row-id row-props)
                        [row-cells row-id row-props]])

(defn view
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-width (keyword, px or string)(opt)
  ;  :cells (multitype-contents in vector)
  ;  :disabled? (boolean)(opt)
  ;  :drag-attributes (map)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :template (string)}
  ;
  ; @usage
  ; [item-list-row {...}]
  ;
  ; @usage
  ; [item-list-row :my-item-list-row {...}]
  ;
  ; @usage
  ; [item-list-row :my-item-list-row {:cells [[:div ]]}]
  ([row-props]
   [view (random/generate-keyword) row-props])

  ([row-id row-props]
   ; @note (tutorials#parameterizing)
   (fn [_ row-props]
       (let [row-props (item-list-row.prototypes/row-props-prototype row-props)]
            [item-list-row row-id row-props]))))
