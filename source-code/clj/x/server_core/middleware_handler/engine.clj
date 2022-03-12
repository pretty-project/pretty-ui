
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.middleware-handler.engine
    (:require [buddy.auth.backends.session             :refer [session-backend]]
              [buddy.auth.middleware                   :refer [wrap-authentication wrap-authorization]]
              [ring.middleware.anti-forgery            :refer [wrap-anti-forgery]]
              [ring.middleware.defaults                :refer [site-defaults wrap-defaults]]
              [ring.middleware.keyword-params          :refer [wrap-keyword-params]]
              [ring.middleware.multipart-params        :refer [wrap-multipart-params]]
              [ring.middleware.params                  :refer [wrap-params]]
              [ring.middleware.reload                  :refer [wrap-reload]]
              [ring.middleware.json                    :refer [wrap-json-body]]
              [ring.middleware.transit                 :refer [wrap-transit-params]]
              [x.server-core.middleware-handler.config :as middleware-handler.config]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn site-defaults-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) site-defaults
  ;
  ; @return (map)
  ;  {:security (map)
  ;    {:anti-forgery (boolean)}}
  [site-defaults]
  (assoc-in site-defaults [:security :anti-forgery] false))



;; -- Middleware --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn middleware
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:middleware (functions in vector)}
  []
  (let [backend       (session-backend) ; For the buddy authenticating services
        site-defaults (site-defaults-prototype site-defaults)]
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
