
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.middleware-handler.helpers
    (:require [buddy.auth.backends.session                 :refer [session-backend]]
              [buddy.auth.middleware                       :refer [wrap-authentication wrap-authorization]]
              [ring.middleware.anti-forgery                :refer [wrap-anti-forgery]]
              [ring.middleware.defaults                    :refer [site-defaults wrap-defaults]]
              [ring.middleware.keyword-params              :refer [wrap-keyword-params]]
              [ring.middleware.multipart-params            :refer [wrap-multipart-params]]
              [ring.middleware.params                      :refer [wrap-params]]
              [ring.middleware.reload                      :refer [wrap-reload]]
              [ring.middleware.json                        :refer [wrap-json-body]]
              [ring.middleware.transit                     :refer [wrap-transit-params]]
              [x.server-core.middleware-handler.config     :as middleware-handler.config]
              [x.server-core.middleware-handler.prototypes :as middleware-handler.prototypes]))



;; -- Middleware --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn middleware
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:middleware (functions in vector)}
  []
  (let [backend       (session-backend) ; For the buddy authenticating services
        site-defaults (middleware-handler.prototypes/site-defaults-prototype site-defaults)]
       {:middleware [#(wrap-reload           % {:dirs middleware-handler.config/SOURCE-DIRECTORY-PATHS})
                     #(wrap-keyword-params   %)
                     #(wrap-params           %)
                     #(wrap-transit-params   % {:opts {}})
                     #(wrap-multipart-params %)
                     #(wrap-authentication   % backend)
                     #(wrap-authorization    % backend)
                     #(wrap-defaults         % site-defaults)
                     #(wrap-json-body        % {:keywords? true})
                     #(wrap-anti-forgery     %)]}))
