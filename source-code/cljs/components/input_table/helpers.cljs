
(ns components.input-table.helpers
    (:require [fruits.hiccup.api :as hiccup]
              [pretty-css.api    :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-label-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:data-color          :default
   :data-font-size      :x
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-indent-bottom  :xs
   :data-indent-left    :xxs})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [style] :as table-props}]
  (-> {:style style}
      (pretty-css/border-attributes table-props)
      (pretty-css/indent-attributes table-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {} (pretty-css/default-attributes table-props)
         (pretty-css/outdent-attributes table-props)))
