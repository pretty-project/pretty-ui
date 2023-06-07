
(ns website.scroll-icon.views
    (:require [random.api                     :as random]
              [website.scroll-icon.attributes :as scroll-icon.attributes]
              [website.scroll-icon.prototypes :as scroll-icon.prototypes]
              [website.scroll-sensor.views    :as scroll-sensor.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scroll-icon
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  [icon-id icon-props]
  [:<> [scroll-sensor.views/component (scroll-icon.attributes/sensor-attributes icon-id icon-props)]
       [:div (scroll-icon.attributes/icon-attributes icon-id icon-props)
             [:div (scroll-icon.attributes/icon-body-attributes icon-id icon-props)]]])

(defn component
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (string)(opt)
  ;   Default: "#FFFFFF"
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [scroll-icon {...}]
  ;
  ; @usage
  ; [scroll-icon :my-scroll-icon {...}]
  ([icon-props]
   [component (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   (let [icon-props (scroll-icon.prototypes/icon-props-prototype icon-props)]
        [scroll-icon icon-id icon-props])))
