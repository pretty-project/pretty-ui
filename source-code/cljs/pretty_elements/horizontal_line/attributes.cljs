
(ns pretty-elements.horizontal-line.attributes
    (:require [css.api        :as css]
              [pretty-css.api :as pretty-css]))

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
      (pretty-css/color-attributes  line-props)
      (pretty-css/indent-attributes line-props)))

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
      (pretty-css/default-attributes      line-props)
      (pretty-css/outdent-attributes      line-props)
      (pretty-css/element-size-attributes line-props)))
