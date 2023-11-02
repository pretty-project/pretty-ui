
(ns components.item-list-header.views
    (:require [components.item-list-header.helpers    :as item-list-header.helpers]
              [components.item-list-header.prototypes :as item-list-header.prototypes]
              [pretty-elements.api                           :as pretty-elements]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-cell
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; @param (integer) cell-dex
  ; @param (map) cell-props
  ; {:label (metamorphic-content)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)}
  [header-id header-props cell-dex {:keys [label] :as cell-props}]
  (let [cell-props (item-list-header.prototypes/cell-props-prototype header-id header-props cell-dex cell-props)]
       [:div.c-item-list-header--cell (item-list-header.helpers/cell-attributes cell-props)
                                      (if label [pretty-elements/label cell-props])]))

(defn- header-cells
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:cells (maps in vector)}
  [header-id {:keys [cells] :as header-props}]
  (letfn [(f [cells cell-dex cell-props]
             (conj cells [header-cell header-id header-props cell-dex cell-props]))]
         (reduce-kv f [:<>] cells)))

(defn- item-list-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  [:div.c-item-list-header (item-list-header.helpers/header-attributes header-id header-props)
                           [header-cells                               header-id header-props]])

(defn component
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {:border (keyword)(opt)
  ;   :bottom, :top}
  ;  :cells (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :on-click (Re-Frame metamorphic-event)(opt)
  ;     :width (px)(opt)}]
  ;  :template (string)}
  ;
  ; @usage
  ; [item-list-header {...}]
  ;
  ; @usage
  ; [item-list-header :my-item-list-header {...}]
  ;
  ; @usage
  ; [item-list-header :my-item-list-header {:cells [[:div ]]}]
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props] ; XXX#0106 (README.md#parametering)
       (let [header-props (item-list-header.prototypes/header-props-prototype header-props)]
            [item-list-header header-id header-props]))))
