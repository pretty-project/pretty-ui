
(ns components.input-table.helpers
    (:require [fruits.hiccup.api                :as hiccup]
              [pretty-attributes.api :as pretty-attributes]))

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
  ;
  ; @return (map)
  [_ table-props]
  (-> {}
      (pretty-attributes/border-attributes table-props)
      (pretty-attributes/indent-attributes table-props)
      (pretty-attributes/style-attributes  table-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-attributes
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> {} (pretty-attributes/class-attributes  table-props)
         (pretty-attributes/outdent-attributes table-props)
         (pretty-attributes/state-attributes  table-props)))
