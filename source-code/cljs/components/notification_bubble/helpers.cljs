
(ns components.notification-bubble.helpers
    (:require [pretty-css.api :as pretty-css]
              [css.api                      :as css]
              [math.api                     :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-body-attributes
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style] :as bubble-props}]
  (merge (pretty-css/indent-attributes bubble-props)
         {:style style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-attributes
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  [_ bubble-props]
  (merge (pretty-css/default-attributes bubble-props)
         (pretty-css/outdent-attributes bubble-props)))
