
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.subs
    (:require [plugins.item-browser.cores.subs   :as cores.subs]
              [plugins.item-lister.download.subs :as plugins.item-lister.download.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.download.subs
(def get-downloaded-items plugins.item-lister.download.subs/get-downloaded-items)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r download.subs/get-resolver-id db :my-extension :my-type :get)
  ;  =>
  ;  :my-handler/get-item
  ;
  ; @return (keyword)
  [db [_ extension-id item-namespace action-key]]
  (let [handler-key (r core.subs/get-meta-item db extension-id item-namespace :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key) "-item"))))
