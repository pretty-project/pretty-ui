
; WARNING! THIS IS AN OUTDATED VERSION OF A MONO-TEMPLATE FILE!

;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-views.api
    (:require [project-emulator.server-views.admin :as admin]
              [project-emulator.server-views.main  :as main]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; project-emulator.server-views.admin
(def admin admin/view)

; project-emulator.server-views.main
(def main main/view)
