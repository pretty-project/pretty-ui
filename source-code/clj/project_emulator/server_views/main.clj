
; WARNING! THIS IS AN OUTDATED VERSION OF A MONO-TEMPLATE FILE!

;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-views.main
    (:require [project-emulator.server-views.head :as head]
              [x.server-ui.api                :as ui]
              [x.server-views.api             :as views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  [request]
  (ui/html (ui/head        request (head/request->head-props request))
           (views/app-main request {})))
