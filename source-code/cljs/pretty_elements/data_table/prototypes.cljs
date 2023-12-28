
(ns pretty-elements.data-table.prototypes
    (:require [fruits.css.api :as css]
              [pretty-build-kit.api :as pretty-build-kit]
              [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-props-prototype
  ; @ignore
  ;
  ; @param (map) cell-props
  ;
  ; @return (map)
  ; {:font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :line-height (keyword)
  ;  :text-overflow (keyword)}
  [cell-props]
  (merge {:font-size     :s
          :font-weight   :normal
          :line-height   :text-block
          :text-overflow :ellipsis
          :content-value-f return
          :placeholder-value-f return}
         (-> cell-props)))

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (map) column-props
  ;
  ; @return (map)
  ; {:template (string)
  ;  :width (keyword, px or string)}
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
  ; {:height (keyword, px or string)
  ;  :template (string)}
  [{:keys [cells] :as row-props}]
  (merge {:height :s
          :template (css/repeat- (count cells) (css/fr 1))}
         (-> row-props)))


(defn table-props-prototype
  ; @ignore
  ;
  ; @param (map) table-props
  ;
  ; @return (map)
  [_])
