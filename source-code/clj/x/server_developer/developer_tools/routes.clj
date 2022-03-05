
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.developer-tools.routes
    (:require [server-fruits.http                       :as http]
              [x.server-developer.developer-tools.views :as developer-tools.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (http/html-wrap {:body (developer-tools.views/view request)}))