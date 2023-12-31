
(ns pretty-elements.chip-group.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
  {:class               :pe-chip-group--chips-placeholder
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
  (-> {:class :pe-chip-group--body
       :style style}
      (pretty-build-kit/indent-attributes group-props)))

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
  (-> {:class :pe-chip-group}
      (pretty-build-kit/class-attributes   group-props)
      (pretty-build-kit/outdent-attributes group-props)
      (pretty-build-kit/state-attributes   group-props)))
