
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.upload.subs
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r download.subs/get-mutation-name db :my-plugin :delete-items)
  ;  =>
  ;  "my-handler/delete-items!"
  ;
  ; @return (string)
  [db [_ plugin-id action-key]]
  (let [handler-key (r transfer.subs/get-transfer-item db plugin-id :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key)))))
