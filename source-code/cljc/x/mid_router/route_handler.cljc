
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.17
; Description:
; Version: v0.2.8
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-router.route-handler
    (:require [mid-fruits.uri :as uri]
              [x.mid-core.api :as a :refer [r]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-home
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (let [app-home (r a/get-app-detail db :app-home)]
       (uri/valid-path app-home)))
