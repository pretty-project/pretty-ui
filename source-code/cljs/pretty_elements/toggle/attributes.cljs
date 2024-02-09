
(ns pretty-elements.toggle.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-body-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-text-overflow (keyword)}
  [_ toggle-props]
  (-> {:class              :pe-toggle--body
       :data-text-overflow :hidden}
      (pretty-attributes/anchor-attributes              toggle-props)
      (pretty-attributes/background-color-attributes             toggle-props)
      (pretty-attributes/border-attributes            toggle-props)
      (pretty-attributes/clickable-state-attributes          toggle-props)
      (pretty-attributes/cursor-attributes            toggle-props)
      (pretty-attributes/effect-attributes           toggle-props)
      (pretty-attributes/full-block-size-attributes      toggle-props)
      (pretty-attributes/indent-attributes            toggle-props)
      (pretty-attributes/mouse-event-attributes       toggle-props)
      (pretty-attributes/react-attributes             toggle-props)
      (pretty-attributes/text-attributes toggle-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ toggle-props]
  (-> {:class :pe-toggle}
      (pretty-attributes/class-attributes       toggle-props)
      (pretty-attributes/outdent-attributes      toggle-props)
      (pretty-attributes/state-attributes       toggle-props)
      (pretty-attributes/theme-attributes        toggle-props)
      (pretty-attributes/wrapper-size-attributes toggle-props)))
