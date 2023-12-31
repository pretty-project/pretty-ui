
(ns pretty-elements.horizontal-line.attributes
    (:require [fruits.css.api :as css]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-body-attributes
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [line-id {:keys [strength style] :as line-props}]
  (-> {:class :pe-horizontal-line--body
       :style (merge style {:height (css/px strength)})}
      (pretty-build-kit/color-attributes        line-props)
      (pretty-build-kit/element-size-attributes line-props)
      (pretty-build-kit/indent-attributes       line-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-attributes
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ line-props]
  (-> {:class :pe-horizontal-line}
      (pretty-build-kit/class-attributes        line-props)
      (pretty-build-kit/outdent-attributes      line-props)
      (pretty-build-kit/state-attributes        line-props)
      (pretty-build-kit/wrapper-size-attributes line-props)))
