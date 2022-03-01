
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.side-effects
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-server!
  ; @param (map)(opt) server-props
  ;  {:join? (boolean)(opt)
  ;    Default: false
  ;   :port (integer or string)(opt)
  ;    Default: DEFAULT-PORT}
  ([]             (start-server! {}))
  ([server-props] (a/dispatch [:boot-loader/start-server! server-props])))
