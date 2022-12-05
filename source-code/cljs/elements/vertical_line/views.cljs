
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.vertical-line.views
    (:require [elements.vertical-line.helpers    :as vertical-line.helpers]
              [elements.vertical-line.prototypes :as vertical-line.prototypes]
              [random.api                        :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-line
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  [line-id line-props]
  [:div.e-vertical-line (vertical-line.helpers/line-attributes line-id line-props)
                        [:div.e-vertical-line--body (vertical-line.helpers/line-body-attributes line-id line-props)]])

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :highlight, :muted, :primary, :secondary
  ;   Default: :muted
  ;  :strength (px)(opt)
  ;   Default: 1
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [vertical-line {...}]
  ;
  ; @usage
  ; [vertical-line :my-vertical-line {...}]
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   (let [line-props (vertical-line.prototypes/line-props-prototype line-props)]
        [vertical-line line-id line-props])))
