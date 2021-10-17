
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.19
; Description:
; Version: v0.4.6
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body
    (:require [hiccup.page        :refer [include-js]]
              [mid-fruits.candy   :refer [param]]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [x.mid-ui.api       :as ui]
              [x.server-core.api  :as a :refer [cache-control-uri]]
              [x.server-ui.shield :refer [view] :rename {view app-shield}]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->core-js-filename
  ; @param (map) request
  ;
  ; @return (string)
  [request]
  (let [core-js-filename         (a/request->route-param request :js)
        default-core-js-filename (a/subscribed [:x.server-core/get-config-item :default-core-js])]
      (string/not-starts-with! (or core-js-filename default-core-js-filename)
                               (param "/"))))

(defn request->core-js-uri-base
  ; @param (map) request
  ;
  ; @return (string)
  [request]
  (let [core-js-dir (a/subscribed [:x.server-core/get-config-item :core-js-dir])]
       (-> core-js-dir (string/starts-with! "/")
                       (string/ends-with!   "/"))))

(defn request->core-js-uri
  ; @param (map) request
  ;
  ; @return (string)
  [request]
  (let [core-js-uri-base (request->core-js-uri-base request)
        core-js-filename (request->core-js-filename request)]
       (str core-js-uri-base core-js-filename)))

(defn request->core-js-props
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:cache-control? (boolean)
  ;   :uri (string)}
  [request]
  (let [core-js-uri (request->core-js-uri request)]
       {:cache-control? true
        :uri            core-js-uri}))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body<-js-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) body
  ; @param (map) request
  ; @param (map) body-props
  ;  {:app-build (string)(opt)
  ;   :plugin-js-paths (maps in vector)
  ;    [{:cache-control? (boolean)(opt)
  ;       Default: false
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [body request {:keys [app-build plugin-js-paths]}]
  (let [core-js-props (request->core-js-props request)
        js-paths      (vector/cons-item plugin-js-paths core-js-props)]
       (reduce (fn [body {:keys [cache-control? size uri]}]
                   (if cache-control?
                       (let [uri (cache-control-uri uri app-build)]
                            (vector/conj-item (param body)
                                              (include-js uri)))
                       (vector/conj-item (param body)
                                         (include-js uri))))
               (param body)
               (param js-paths))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:shield (hiccup)}
  [request body-props]
  (merge (a/subscribed [:x.server-core/get-destructed-configs])
         {:shield (app-shield (ui/loading-animation-a))}
         (param body-props)))
