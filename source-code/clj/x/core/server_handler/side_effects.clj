
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.server-handler.side-effects
    (:require [org.httpkit.server               :refer [run-server]]
              [re-frame.api                     :as r]
              [x.app-details                    :as x.app-details]
              [x.core.router-handler.helpers    :refer [ring-handler]]
              [x.core.server-handler.prototypes :as server-handler.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn run-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;  {:dev-mode? (boolean)(opt)
  ;    Default: false
  ;   :join? (boolean)(opt)
  ;   :port (integer)(opt)}
  [server-props]
  (let [server-props (server-handler.prototypes/server-props-prototype server-props)
        server-state (-> (ring-handler)
                         (run-server server-props))]
       (r/dispatch [:x.core/store-server-state! server-state])
       ; *
       (let [server-port (get server-props :port)]
            (println x.app-details/app-codename "started on port:" server-port))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.core/run-server! run-server!)