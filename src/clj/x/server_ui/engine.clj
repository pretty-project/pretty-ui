
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.19
; Description:
; Version: v0.3.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.engine
    (:require [mid-fruits.css :as css]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn include-favicon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filepath
  ; @param (string) sizes
  ;
  ; @usage
  ;  (include-favicon "/favicon.ico" "16x16")
  ;
  ; @return (vector)
  [filepath sizes]
  [:link {:rel "icon" :type "image/png" :href filepath :sizes sizes}])

(defn include-font
  ; WARNING! AVOID CSS IMPORT!
  ;
  ; https://gtmetrix.com/avoid-css-import.html
  ; Using CSS @import in an external stylesheet can add additional delays
  ; during the loading of a web page.
  ;
  ; @param (string) url
  ;
  ; @usage
  ;  (include-font "https://example.com/style.css")
  ;  => [:style {:type "text/css"} "@import url('https://example.com/style.css')"]
  ;
  ; @return (vector)
  [url]
  [:style {:type "text/css"} (str "@import " (css/url url))])
