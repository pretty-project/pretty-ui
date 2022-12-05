
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.horizontal-line.views
    (:require [elements.horizontal-line.helpers    :as horizontal-line.helpers]
              [elements.horizontal-line.prototypes :as horizontal-line.prototypes]
              [random.api                          :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-line
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  [line-id line-props]
  [:div.e-horizontal-line (horizontal-line.helpers/line-attributes line-id line-props)
                          [:div.e-horizontal-line--body (horizontal-line.helpers/line-body-attributes line-id line-props)]])

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
  ; [horizontal-line {...}]
  ;
  ; @usage
  ; [horizontal-line :my-horizontal-line {...}]
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   (let [line-props (horizontal-line.prototypes/line-props-prototype line-props)]
        [horizontal-line line-id line-props])))
