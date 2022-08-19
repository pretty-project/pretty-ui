
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns forms.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:environment/add-css! {:uri "/css/forms/style.css"}]})
