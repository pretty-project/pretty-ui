
(ns components.side-menu-button.presets)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BUTTON-PROPS-PRESETS
     {:add       {:icon        :add
                  :label       :add!}
      :delete    {:color       :warning
                  :icon        :delete_outline
                  :label       :delete!}
      :duplicate {:icon        :file_copy
                  :icon-family :material-icons-outlined
                  :label       :duplicate!}
      :edit      {:icon        :edit
                  :label       :edit!}
      :revert    {:icon        :settings_backup_restore
                  :label       :revert!}
      :save      {:icon        :save
                  :icon-family :material-icons-outlined
                  :label       :save!}})
