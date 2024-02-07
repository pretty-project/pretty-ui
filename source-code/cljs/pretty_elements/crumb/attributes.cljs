
(ns pretty-elements.crumb.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-label-attributes
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ crumb-props]
  (-> {:class :pe-crumb--label}
      (pretty-attributes/font-attributes crumb-props)
      (pretty-attributes/text-attributes crumb-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-body-attributes
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [crumb-id crumb-props]
  (-> {:class :pe-crumb--body}
      (pretty-attributes/anchor-attributes          crumb-props)
      (pretty-attributes/clickable-state-attributes crumb-props)
      (pretty-attributes/effect-attributes          crumb-props)
      (pretty-attributes/flex-attributes            crumb-props)
      (pretty-attributes/full-block-size-attributes crumb-props)
      (pretty-attributes/indent-attributes          crumb-props)
      (pretty-attributes/mouse-event-attributes     crumb-props)
      (pretty-attributes/state-attributes           crumb-props)
      (pretty-attributes/style-attributes           crumb-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-attributes
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ crumb-props]
  (-> {:class :pe-crumb}
      (pretty-attributes/outdent-attributes      crumb-props)
      (pretty-attributes/class-attributes        crumb-props)
      (pretty-attributes/state-attributes        crumb-props)
      (pretty-attributes/theme-attributes        crumb-props)
      (pretty-attributes/wrapper-size-attributes crumb-props)))
