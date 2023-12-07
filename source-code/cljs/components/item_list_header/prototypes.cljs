
(ns components.item-list-header.prototypes
    (:require [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @param (map) header-props
  ; {:cells (vector)}
  ;
  ; @return (map)
  ; {:cells (vector)}
  [{:keys [cells] :as header-props}]
  ; XXX#0561 (source-code/app/pretty_components/frontend/item_list_row/prototypes.cljs)
  (merge {}
         (-> header-props)
         {:cells (vector/remove-items-by cells nil?)}))

(defn cell-props-prototype
  ; @param (map) cell-props
  ;
  ; @return (map)
  ; {}
  [_ {:keys [order-by]} _ {:keys [order-by-key label] :as cell-props}]
  (let [order-by? (and order-by order-by-key (= (name order-by-key) (namespace order-by)))]
       (merge {:color         (if order-by? :default :muted)
               :icon-position (if order-by? :right)
               :icon          (if order-by? (case (name order-by) "descending" :arrow_drop_down "ascending" :arrow_drop_up))
               :indent        (if order-by? {:horizontal :xs} {:all :xs})
               :horizontal-align :left
               :font-size        :xs}
              (-> cell-props)
              {:content label})))
