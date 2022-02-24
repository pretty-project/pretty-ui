
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.23
; Description:
; Version: v0.8.8
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.title.subs
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-window-title-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-content) window-title
  ;
  ; @return (string)
  [db [_ window-title]]
  (if-let [window-title (r components/get-metamorphic-value db window-title)]
          (let [app-title (r a/get-app-config-item db :app-title)]
               (str window-title " - " app-title))
          (r a/get-app-config-item db :app-title)))
