
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.media-browser.lifecycles
    (:require [server-extensions.storage.engine :as engine]
              [x.server-core.api                :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:router/add-route! :storage.media-browser/route
                                                     {:route-template "/@app-home/storage"
                                                      :client-event [:storage.media-browser/load-browser!]
                                                      :restricted? true}]
                                 [:router/add-route! :storage.media-browser/extended-route
                                                     {:route-template "/@app-home/storage/:media-id"
                                                      :client-event [:storage.media-browser/load-browser!]
                                                      :restricted? true}]]}})
