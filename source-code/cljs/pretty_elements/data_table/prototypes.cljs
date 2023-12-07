
(ns pretty-elements.data-table.prototypes
    (:require [fruits.css.api :as css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (map) column-props
  ;
  ; @return (map)
  ; {:template (string)
  ;  :width (keyword)}
  [{:keys [cells] :as column-props}]
  (merge {:template (css/repeat- (count cells) (css/fr 1))
          :width :s}
         (-> column-props)))

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {:height (keyword)
  ;  :template (string)}
  [{:keys [cells] :as row-props}]
  (merge {:height :s
          :template (css/repeat- (count cells) (css/fr 1))}
         (-> row-props)))

(defn cell-props-prototype
  ; @ignore
  ;
  ; @param (map) cell-props
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :font-weight (keyword)
  ;  :line-height (keyword)
  ;  :text-overflow (keyword)}
  [cell-props]
  (merge {:font-size     :s
          :font-weight   :normal
          :line-height   :text-block
          :text-overflow :ellipsis}
         (-> cell-props)))

(defn table-props-prototype
  ; @ignore
  ;
  ; @param (map) table-props
  ;
  ; @return (map)
  [_])
