
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.side-effects
    (:require [app-fruits.window :as window]
              [mid-fruits.uri    :as uri]
              [x.app-core.debug-handler.engine :as engine]
              [x.app-core.event-handler        :as event-handler :refer [r]]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- detect-debug-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [uri          (window/get-uri)
        query-string (uri/uri->query-string uri)]
       (event-handler/dispatch [:db/set-item! [:core :debug-handler/meta-items :debug-mode]
                                              (engine/query-string->debug-mode query-string)])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/detect-debug-mode! detect-debug-mode!)
