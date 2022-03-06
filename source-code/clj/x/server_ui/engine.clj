
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.engine
    (:require [mid-fruits.css :as css]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn include-js
  ; @param (map) include-props
  ;  {:uri (string)}
  ;
  ; @usage
  ;  (ui/include-js {:uri "/my-script.js"})
  [{:keys [uri]}]
  [:script {:type "text/javascript" :src uri}])

(defn include-css
  ; @param (map) include-props
  ;  {:on-load (function)(opt)
  ;   :uri (string)}
  ;
  ; @usage
  ;  (ui/include-css {:uri "/my-style.css"})
  [{:keys [on-load uri]}]
  [:link {:type "text/css" :href uri :rel "stylesheet" :on-load on-load}])

(defn include-favicon
  ; @param (map) include-props
  ;  {:uri (string)
  ;   :size (string)}
  ;
  ; @usage
  ;  (ui/include-favicon {:uri  "/my-favicon.ico"
  ;                       :size "16x16"})
  [{:keys [uri size]}]
  [:link {:rel "icon" :type "image/png" :href uri :sizes size}])

(defn include-font
  ; https://gtmetrix.com/avoid-css-import.html
  ; Using CSS @import in an external stylesheet can add additional delays
  ; during the loading of a web page.
  ;
  ; @param (map) include-props
  ;  {:uri (string)}
  ;
  ; @usage
  ;  (ui/include-font {:uri "/my-style.css"})
  ;  =>
  ;  [:style {:type "text/css"} "@import url('/my-style.css')"]
  [{:keys [uri]}]
  [:style {:type "text/css"} (str "@import " (css/url uri))])
