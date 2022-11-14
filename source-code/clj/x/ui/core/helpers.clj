
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.core.helpers
    (:require [css.api :as css]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn include-js
  ; @param (map) include-props
  ;  {:id (keyword)
  ;   :uri (string)}
  ;
  ; @usage
  ;  (include-js {:uri "/my-script.js"})
  [{:keys [id uri]}]
  [:script {:id id :type "text/javascript" :src uri}])

(defn include-css
  ; @param (map) include-props
  ;  {:id (keyword)
  ;   :on-load (function)(opt)
  ;   :uri (string)}
  ;
  ; @usage
  ;  (include-css {:uri "/my-style.css"})
  [{:keys [id on-load uri]}]
  [:link {:id id :type "text/css" :href uri :rel "stylesheet" :on-load on-load}])

(defn include-favicon
  ; @param (map) include-props
  ;  {:id (keyword)
  ;   :size (string)
  ;   :uri (string)}
  ;
  ; @usage
  ;  (include-favicon {:uri  "/my-favicon.ico"
  ;                    :size "16x16"})
  [{:keys [id size uri]}]
  [:link {:id id :rel "icon" :type "image/png" :href uri :sizes size}])

(defn include-font
  ; https://gtmetrix.com/avoid-css-import.html
  ; Using CSS @import in an external stylesheet can add additional delays
  ; during the loading of a web page.
  ;
  ; @param (map) include-props
  ;  {:id (keyword)
  ;   :uri (string)}
  ;
  ; @usage
  ;  (include-font {:uri "/my-style.css"})
  ;  =>
  ;  [:style {:type "text/css"} "@import url('/my-style.css')"]
  [{:keys [id uri]}]
  [:style {:id id :type "text/css"} (str "@import " (css/url uri))])
