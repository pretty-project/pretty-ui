
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.docs.routes
    (:require [server-fruits.http             :as http]
              [x.server-developer.docs.engine :as docs.engine]
              [x.server-user.api              :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-docs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (if (user/request->authenticated? request)
      (http/map-wrap {:body (docs.engine/read-namespaces docs.engine/ROOT-DIRECTORY-PATH)})))
