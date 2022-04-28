
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.subs
    (:require [extensions.clients.client-viewer.subs :as client-viewer.subs]
              [x.app-core.api                        :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; extensions.clients.client-viewer.subs
(def get-client-name client-viewer.subs/get-client-name)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :clients.client-editor/get-client-name get-client-name)
