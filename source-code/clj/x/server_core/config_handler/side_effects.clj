
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler.side-effects
    (:require [server-fruits.io                    :as io]
              [x.server-core.config-handler.config :as config-handler.config]
              [x.server-core.event-handler         :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- config-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [app-config    (io/read-edn-file config-handler.config/APP-CONFIG-FILEPATH)
        server-config (io/read-edn-file config-handler.config/SERVER-CONFIG-FILEPATH)
        site-config   (io/read-edn-file config-handler.config/SITE-CONFIG-FILEPATH)]
       (event-handler/dispatch [:core/store-configs! {:app-config    app-config
                                                      :server-config server-config
                                                      :site-config   site-config}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/config-server! config-server!)
