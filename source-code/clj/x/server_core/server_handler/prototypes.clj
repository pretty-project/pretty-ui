
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.server-handler.prototypes
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.string :as string]
              [re-frame.api      :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;  {:port (integer, nil or string)}
  ;
  ; @return (map)
  ;  {:port (integer)
  ;   :max-body (integer)
  [{:keys [port] :as server-props}]
  (let [default-port @(r/subscribe [:core/get-server-config-item :default-port])
        max-body     @(r/subscribe [:core/get-server-config-item :max-body])]
       (merge {:max-body max-body}
              (param server-props)
              (cond (string/nonempty? port) {:port (string/to-integer port)}
                    (nil?             port) {:port default-port}))))
