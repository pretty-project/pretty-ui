
(ns pretty-elements.toggle.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
  ;  :data-click-effect (keyword)
  ;  :data-text-overflow (keyword)}
  [_ toggle-props]
  (-> {:class              :pe-toggle--body
       :data-click-effect  :opacity
       :data-text-overflow :hidden}
      (pretty-build-kit/border-attributes       toggle-props)
      (pretty-build-kit/color-attributes        toggle-props)
      (pretty-build-kit/cursor-attributes       toggle-props)
      (pretty-build-kit/element-size-attributes toggle-props)
      (pretty-build-kit/focus-attributes        toggle-props)
      (pretty-build-kit/indent-attributes       toggle-props)
      (pretty-build-kit/link-attributes         toggle-props)
      (pretty-build-kit/mouse-event-attributes  toggle-props)
      (pretty-build-kit/unselectable-attributes toggle-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ toggle-props]
  (-> {:class :pe-toggle}
      (pretty-build-kit/class-attributes        toggle-props)
      (pretty-build-kit/outdent-attributes      toggle-props)
      (pretty-build-kit/state-attributes        toggle-props)
      (pretty-build-kit/wrapper-size-attributes toggle-props)))
