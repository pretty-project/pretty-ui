
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.4.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.events
    (:require [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-lister.events :as events]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.events
(def store-lister-props! events/store-lister-props!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn initialize-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
  (r store-lister-props! db extension-id item-namespace lister-props))
