
(ns elements.chip-group.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-group-placeholder-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  {:class               :e-chip-group--chips-placeholder
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :text-block})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-group-body-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as group-props}]
  (-> {:class :e-chip-group--body
       :style style}
      (pretty-css/indent-attributes group-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-group-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ group-props]
  (-> {:class :e-chip-group}
      (pretty-css/default-attributes group-props)
      (pretty-css/outdent-attributes group-props)))
