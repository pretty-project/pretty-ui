
(ns components.item-list-row.views
    (:require [components.item-list-row.helpers    :as item-list-row.helpers]
              [components.item-list-row.prototypes :as item-list-row.prototypes]
              [css.api                             :as css]
              [elements.api                        :as elements]
              [random.api                          :as random]
              [x.components.api                    :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-cells
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:cells (vector)}
  [row-id {:keys [cells]}]
  (letfn [(f [cells cell-props]
             (conj cells [x.components/content row-id cell-props]))]
         (reduce f [:<>] cells)))

(defn- item-list-row
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:border (keyword)(opt)
  ;  :cells (components and/or symbols in vector)}
  [row-id {:keys [border cells] :as row-props}]
  [:div.c-item-list-row (item-list-row.helpers/row-attributes row-id row-props)
                        [row-cells row-id row-props]])

(defn component
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ; {:border (keyword)(opt)
  ;   :bottom, :top}
  ;  :cells (metamorphic-contents in vector)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :drag-attributes (map)(opt)
  ;  :highlighted? (boolean)(opt)
  ;   Default: false
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl
  ;    Default: :tr
  ;    W/ {:marker-color ...}
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
   [component (random/generate-keyword) row-props])

  ([row-id row-props]
   (let [row-props (item-list-row.prototypes/row-props-prototype row-props)]
        [item-list-row row-id row-props])))
