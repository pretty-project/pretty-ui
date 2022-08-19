
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.server-handler.prototypes
    (:require [mid-fruits.candy                    :refer [param]]
              [mid-fruits.string                   :as string]
              [x.server-core.server-handler.config :as server-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;  {:port (integer or string)}
  ;
  ; @return (map)
  ;  {:port (integer)
  ;   :max-body (integer)
  [{:keys [port] :as server-props}]
  (merge {:port server-handler.config/DEFAULT-PORT
          ; {:max-body ...} is very important for projects which want to upload at least 1GB
          :max-body server-handler.config/MAX-BODY}
         (param server-props)
         (if (string/nonempty? port)
             {:port (string/to-integer port)})))
