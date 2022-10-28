
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-router.route-handler.prototypes
    (:require [mid-fruits.candy                      :refer [param return]]
              [re-frame.api                          :refer [r]]
              [x.server-core.api                     :as x.core]
              [x.server-router.route-handler.config  :as route-handler.config]
              [x.server-router.route-handler.helpers :as route-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handler-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (function or map) handler
  ; @param (map) options
  ;  {:restricted? (boolean)(opt)}
  ;
  ; @example
  ;  (route-handler.prototypes/handler-prototype (fn [] ...) {...})
  ;  =>
  ;  {:handler (fn [] ...)}
  ;
  ; @example
  ;  (route-handler.prototypes/handler-prototype {:handler (fn [] ...)} {...})
  ;  =>
  ;  {:handler (fn [] ...)}
  ;
  ; @return (map)
  ;  {:handler (function)}
  [handler {:keys [restricted?]}]
  ; Ha az útvonal {:restricted? true} beállítással lett hozzádva, akkor a handler függvényt
  ; körbeveszi a route-authenticator függvénnyel.
  (if restricted? (cond (fn?  handler) (return {:handler (route-handler.helpers/route-authenticator           handler)})
                        (map? handler) (assoc   :handler (route-handler.helpers/route-authenticator (:handler handler))))
                  (cond (fn?  handler) (return {:handler handler})
                        (map? handler) (return handler))))

(defn route-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  ;  {:get (function or map)(opt)
  ;   :post (function or map)(opt)
  ;   :restricted? (boolean)(opt)
  ;   :route-parent (string)(opt)}
  ;
  ; @return (map)
  ;  {:get (map)
  ;   :js-build (keyword)
  ;   :post (map)
  ;   :route-parent (string)}
  [db [_ _ {:keys [get post restricted? route-parent] :as route-props}]]
  (let [app-home (r x.core/get-app-config-item db :app-home)]
       (merge {:js-build route-handler.config/DEFAULT-JS-BUILD}
              (param route-props)
              (if route-parent {:route-parent (route-handler.helpers/resolve-variable-route-string route-parent app-home)})
              (if get          {:get          (handler-prototype get  {:restricted? restricted?})})
              (if post         {:post         (handler-prototype post {:restricted? restricted?})}))))
